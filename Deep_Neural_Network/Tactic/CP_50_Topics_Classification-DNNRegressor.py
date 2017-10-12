
# coding: utf-8

# In[2]:




# In[7]:

#  Copyright 2016 The TensorFlow Authors. All Rights Reserved.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

"""Example of DNNClassifier for CHECKPOINT 50 Topics dataset."""

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
import tarfile
import os
import pip
pip.main(['install', 'sklearn'])
pip.main(['install', 'pandas'])
import pandas as pd
from sklearn import cross_validation
from sklearn import metrics
from sklearn import datasets
import tensorflow as tf
from tensorflow.contrib import learn
import tensorflow.contrib.learn as skflow
import numpy as np
from numpy import linalg
from math import sqrt

# Load dataset.

def import_data():
    if "Sorted_Num_CP50" not in os.listdir(os.getcwd()):
        # Untar directory of Sorted_Num_CP50 if we haven't already
        tarObject = tarfile.open("Sorted_Num_CP50.tar.gz")
        tarObject.extractall()
        tarObject.close()
        print("Extracted tar to current directory")
    else:
        # we've already extracted the files
        pass

    print("loading training data")
    df_data = pd.read_csv("Sorted_Num_CP50.csv", usecols=[2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,
                                                     21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,
                                                     38,39,40,41,42,43,44,45,46,47,48,49,50],dtype='int32')
                       
    df_label = pd.read_csv("Sorted_Num_CP50.csv", usecols=[51],dtype='int32')
    return df_data,df_label

df_data,df_label = import_data()

df_label_NM = df_label.as_matrix()
df_label_NM_1D = df_label_NM.ravel()
df_data_NM = df_data.as_matrix()

skf = cross_validation.StratifiedKFold(df_label_NM_1D,n_folds=10)
fold_accuracy = []
fold_precision = []
fold_recall = []
fold_MSE  = []
fold_RMSE = []
fold_MAE = []
fold_confusion_matrix = np.array([[0,0],[0,0]])
fold_number = 1
for train_index, test_index in skf:
    print("Fold Number:",fold_number)
    fold_number+=1
    x_train1, x_test1 = df_data_NM[train_index], df_data_NM[test_index]
    y_train1, y_test1 = df_label_NM_1D[train_index], df_label_NM_1D[test_index]
    
    print(len(y_test1))
    # Build 3 layer DNN with 30 units.
    classifier = skflow.TensorFlowDNNRegressor(hidden_units=[30],n_classes=2, steps=50000) 
    
    # Fit
    classifier.fit(x_train1, y_train1)
    
    score_accracy = metrics.accuracy_score(y_test1, classifier.predict(x_test1))
    fold_accuracy.append(score_accracy)

    score_precision = metrics.precision_score(y_test1,classifier.predict(x_test1))
    fold_precision.append(score_precision)

    score_recall = metrics.recall_score(y_test1,classifier.predict(x_test1))
    fold_recall.append(score_recall)

    score_MSE = metrics.mean_squared_error(y_test1, classifier.predict(x_test1))
    fold_MSE.append(score_MSE)
    
    
    # np.sqrt(((y_test1 - classifier.predict(x_test1)) ** 2).mean())
    # mean_squared_error(y_test1 - classifier.predict(x_test1))**0.5
    # np.linalg.norm(y_test1 - classifier.predict(x_test1)) / np.sqrt(len(y_test1))
    
    score_RMSE = np.linalg.norm(classifier.predict(x_test1) - y_test1) / np.sqrt(len(y_test1))
    fold_RMSE.append(score_RMSE)
   
    score_MAE = metrics.mean_absolute_error(y_test1, classifier.predict(x_test1), multioutput='raw_values')
    fold_MAE.append(score_MAE)
    
    score_CM = np.array(metrics.confusion_matrix(y_test1, classifier.predict(x_test1)))
    fold_confusion_matrix+=score_CM


print("Accuracy per fold: ", fold_accuracy)
print("Average accuracy: ", sum(fold_accuracy)/len(fold_accuracy), "\n")

print("Precision per fold: ", fold_precision)
print("Average precision: ", sum(fold_precision)/len(fold_precision), "\n")

print("Recall per fold: ", fold_recall)
print("Average Recall: ", sum(fold_recall)/len(fold_recall), "\n")

print("MSE per fold: ", fold_MSE)
print("Average MSE: ", sum(fold_MSE)/len(fold_MSE), "\n")

print("RMSE per fold: ", fold_RMSE)
print("Average RMSE: ", sum(fold_RMSE)/len(fold_RMSE), "\n")

print("MAE per fold: ", fold_MAE)
print("Average MAE: ", sum(fold_MAE)/len(fold_MAE), "\n")

print('ROC MATRIX:')
print(fold_confusion_matrix)


