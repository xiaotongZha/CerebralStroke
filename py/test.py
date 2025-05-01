from sklearn.linear_model import LogisticRegression
from sklearn.preprocessing import StandardScaler
from sklearn.feature_selection import RFE

import numpy as np
import os
from sklearn.base import clone
ROOT = os.path.dirname(os.path.abspath(__file__))

def train_and_test():
    X_train = np.load(os.path.join(ROOT, 'processdata', 'X.npy'))
    y_train = np.load(os.path.join(ROOT, 'processdata', 'y.npy'))
    X_test = np.load(os.path.join(ROOT, 'processdata', 'X_test.npy'))
    scaler = StandardScaler()
    X_train = scaler.fit_transform(X_train)
    X_test = scaler.transform(X_test)
    
    clf = LogisticRegression(max_iter=500, random_state=42)
    selector = RFE(clf, n_features_to_select=25)
    selector.fit(X_train, y_train)
    X_train_sel = selector.transform(X_train)
    X_test_sel = selector.transform(X_test)

    clf.fit(X_train_sel, y_train)
    y_proba = clf.predict_proba(X_test_sel)[:, 1].round(3)

    with open(os.path.join(ROOT, 'sharedata', 'testset_predict.txt'), mode='w') as file:
        file.write(' '.join([str(x) for x in y_proba]) + '\n')

if __name__=='__main__':
    train_and_test()

