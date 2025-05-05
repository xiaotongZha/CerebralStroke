import csv
from datetime import datetime
import numpy as np
import os
# -------------------- 数据加载与预处理 --------------------

ROOT = os.path.dirname(os.path.abspath(__file__))

def load_firstExam():
    input_file = os.path.join(ROOT, 'rawdata', 'FirstExam.csv')
    result = {}
    with open(input_file, mode='r', encoding='utf-8') as file:
        reader = csv.reader(file)
        next(reader)
        for row in reader:
            col2 = row[1]
            col3 = row[2]
            col4 = float(row[3]) / 1e3
            col2_datetime = datetime.strptime(col2, '%Y-%m-%d %H:%M:%S')
            result[col3] = (col2_datetime, col4)
    return result

def load_otherExam():
    input_file = os.path.join(ROOT, 'rawdata', 'OtherExam.csv')
    result = {}
    with open(input_file, mode='r', encoding='utf-8') as file:
        reader = csv.reader(file)
        next(reader)
        for row in reader:
            col2 = row[1]
            col3 = row[2]
            col4 = float(row[3]) / 1e3
            col2_datetime = datetime.strptime(col2, '%Y-%m-%d %H:%M:%S')
            if col3 not in result:
                result[col3] = []
            result[col3].append((col2_datetime, col4))
    return result

def is_within_48_hours(time1, time2):
    delta = abs((time2 - time1).total_seconds())
    return delta <= 48 * 3600

def find_matching_patient(first_exam, other_exams):
    patient_id1, patient_id2 = [], []
    for key, (time0, vol0) in first_exam.items():
        if key not in other_exams:
            continue
        hastime, ok = False, True
        for timex, volx in other_exams[key]:
            if not is_within_48_hours(time0, timex):
                break
            hastime = True
            if volx - vol0 > 6 or (volx - vol0) / vol0 >= 0.33:
                patient_id1.append(key)
                ok = False
                break
        if ok and hastime:
            patient_id2.append(key)
    return patient_id1, patient_id2

def merge_patient_data():
    first_exam_file = os.path.join(ROOT, 'rawdata', 'FirstExam.csv')
    patient_file = os.path.join(ROOT, 'rawdata', 'Patient.csv')
    patient_data = {}
    with open(first_exam_file, mode='r', encoding='utf-8') as file:
        reader = csv.reader(file)
        next(reader)
        for row in reader:
            patient_id = row[2]
            other_info = row[3:]
            patient_data[patient_id] = other_info

    with open(patient_file, mode='r', encoding='utf-8') as file:
        reader = csv.reader(file)
        next(reader)
        for row in reader:
            patient_id = row[0]
            gender = 1 if row[2] == 'F' else 0
            other_info = [row[1], gender] + row[3:]
            if patient_id in patient_data:
                patient_data[patient_id] = other_info + patient_data[patient_id]
    return patient_data

def load_dataset():
    patient_dict = merge_patient_data()

    patient_pos, patient_neg = find_matching_patient(load_firstExam(), load_otherExam())

    with open(os.path.join(ROOT, 'sharedata','trainset_label.txt'), mode='w') as file:
        file.write(' '.join(patient_pos) + '\n')
        file.write(' '.join(patient_neg) + '\n')

    patient_pos = [patient_dict[x] for x in patient_pos]
    patient_neg = [patient_dict[x] for x in patient_neg]
    patient_pos = np.array(patient_pos, dtype=float)
    patient_neg = np.array(patient_neg, dtype=float)
    X = np.vstack((patient_pos, patient_neg))
    y = np.array([1]*len(patient_pos) + [0]*len(patient_neg))
    return X, y

def load_testset():
    patient_dict = merge_patient_data()
    with open(os.path.join(ROOT, 'sharedata','testset_name.txt'), mode='w') as file:
        file.write(' '.join(patient_dict.keys()) + '\n')
    patient_matrix=np.array([_ for _ in patient_dict.values()])
    n,m=patient_matrix.shape
    for i in range(n):
        for j in range(m):
            try:
                patient_matrix[i][j]=float(patient_matrix[i][j])
            except:
                patient_matrix[i][j]=0.0
    return np.array(patient_matrix, dtype=float)

if __name__=="__main__":
    if not os.path.exists(os.path.join(ROOT, 'processdata')):
        os.makedirs(os.path.join(ROOT, 'processdata'))
    if not os.path.exists(os.path.join(ROOT, 'sharedata')):
        os.makedirs(os.path.join(ROOT, 'sharedata'))
    if not os.path.exists(os.path.join(ROOT, 'sharedata','pic')):
        os.makedirs(os.path.join(ROOT, 'sharedata','pic'))
    if not os.path.exists(os.path.join(ROOT, 'rawdata')):
        os.makedirs(os.path.join(ROOT, 'rawdata'))
    
    X, y = load_dataset()
    np.save(os.path.join(ROOT, 'processdata', 'X.npy'), X)
    np.save(os.path.join(ROOT, 'processdata', 'y.npy'), y)
    X_test = load_testset()
    np.save(os.path.join(ROOT, 'processdata', 'X_test.npy'), X_test)
