from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, recall_score, precision_score, f1_score, roc_curve, auc
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import KFold
from sklearn.feature_selection import RFE
from sklearn.base import clone
import numpy as np
import matplotlib.pyplot as plt
import os
ROOT = os.path.dirname(os.path.abspath(__file__))


def train_and_eval():
    X = np.load(os.path.join(ROOT, 'processdata', 'X.npy'))
    y = np.load(os.path.join(ROOT, 'processdata', 'y.npy'))

    scaler = StandardScaler()
    X = scaler.fit_transform(X)

    clf = LogisticRegression(max_iter=500, random_state=42)

    scores_all = []
    tpr_list = []
    auc_list = []
    mean_fpr = np.linspace(0, 1, 100)

    seed_list = [5, 10, 12, 13]
    for seed in seed_list:
        kf = KFold(n_splits=5, shuffle=True, random_state=seed)
        fold = 0
        for train_idx, test_idx in kf.split(X):
            print(f"seed {seed} ; fold {fold+1}")
            fold += 1
            X_train, X_test = X[train_idx], X[test_idx]
            y_train, y_test = y[train_idx], y[test_idx]

            clf_clone = clone(clf)
            try:
                selector = RFE(clf, n_features_to_select=25)
                selector.fit(X_train, y_train)
                X_train_sel = selector.transform(X_train)
                X_test_sel = selector.transform(X_test)
            except Exception as e:
                print(f"RFE 失败，跳过本折: {e}")
                continue

            clf_clone.fit(X_train_sel, y_train)
            y_pred = clf_clone.predict(X_test_sel)
            y_proba = clf_clone.predict_proba(X_test_sel)[:, 1]  # for ROC
            print(y_proba)
            # 评分
            scores_all.append([
                accuracy_score(y_test, y_pred),
                recall_score(y_test, y_pred),
                precision_score(y_test, y_pred),
                f1_score(y_test, y_pred)
            ])

            # ROC 数据
            fpr, tpr, _ = roc_curve(y_test, y_proba)
            interp_tpr = np.interp(mean_fpr, fpr, tpr)
            interp_tpr[0] = 0.0
            tpr_list.append(interp_tpr)
            auc_list.append(auc(fpr, tpr))

    # 打印平均评分
    avg_scores = np.mean(scores_all, axis=0)
    print(f"Accuracy: {avg_scores[0]:.4f}, Recall: {avg_scores[1]:.4f}, Precision: {avg_scores[2]:.4f}, F1 Score: {avg_scores[3]:.4f}")
    
    return avg_scores, tpr_list, auc_list, mean_fpr

def plot_metrics(avg_scores):
    metrics = ['Accuracy', 'Recall', 'Precision', 'F1 Score']
    avg_scores_rounded = [round(score, 4) for score in avg_scores]

    plt.figure(figsize=(8, 6))
    bars = plt.bar(metrics, avg_scores_rounded, color=['skyblue', 'lightgreen', 'salmon', 'gold'], edgecolor='black')

    for bar, score in zip(bars, avg_scores_rounded):
        plt.text(bar.get_x() + bar.get_width() / 2, bar.get_height() + 0.01, f'{score:.4f}', 
                 ha='center', va='bottom', fontsize=10)

    plt.title('Model Performance Metrics', fontsize=14, fontweight='bold')
    plt.ylabel('Score', fontsize=12)
    plt.ylim(0, 1.1)
    plt.grid(axis='y', linestyle='--', alpha=0.7)

    plt.tight_layout()
    outpath=os.path.join(ROOT, 'sharedata','pic', 'four_metrics.png')
    plt.savefig(outpath, dpi=300)
    plt.show()

def plot_roc(tpr_list, auc_list, mean_fpr):
    plt.figure(figsize=(8, 6))
    mean_tpr = np.mean(tpr_list, axis=0)
    mean_tpr[-1] = 1.0
    mean_auc = np.mean(auc_list)

    for i, tpr in enumerate(tpr_list):
        plt.plot(mean_fpr, tpr, color='grey', alpha=0.3)

    plt.plot(mean_fpr, mean_tpr, color='blue', label=f'Mean ROC (AUC = {mean_auc:.4f})', linewidth=2)
    plt.plot([0, 1], [0, 1], linestyle='--', color='black', alpha=0.7)

    plt.xlabel('False Positive Rate')
    plt.ylabel('True Positive Rate')
    plt.title('Receiver Operating Characteristic (ROC)')
    plt.legend(loc='lower right')
    plt.grid(alpha=0.3)
    plt.tight_layout()
    outpath=os.path.join(ROOT, 'sharedata','pic', 'roc_curve.png')
    plt.savefig(outpath, dpi=300)
    plt.show()


if __name__=='__main__':
    avg_scores, tpr_list, auc_list, mean_fpr = train_and_eval()
    plot_metrics(avg_scores)
    plot_roc(tpr_list, auc_list, mean_fpr)
