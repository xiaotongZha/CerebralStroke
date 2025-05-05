from train_and_eval import train_and_eval,plot_metrics,plot_roc
from test import train_and_test
if __name__=='__main__':
    avg_scores, tpr_list, auc_list, mean_fpr = train_and_eval()
    plot_metrics(avg_scores)
    plot_roc(tpr_list, auc_list, mean_fpr)
    train_and_test()