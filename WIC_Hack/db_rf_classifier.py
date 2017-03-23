import pandas as pd
from sklearn import cross_validation
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn import metrics

###
### Seperate Target and Outcome Features
###

df_data = pd.read_csv("C:\MS Software Engineering\Spring 2017\WIC Hack\Flask\intuit\i_dataset.csv", usecols=[1,2,3,4,5,8])
df_label = pd.read_csv("C:\MS Software Engineering\Spring 2017\WIC Hack\Flask\intuit\i_dataset.csv", usecols=[9])

df_label_NM = df_label.as_matrix()
df_label_NM_1D = df_label_NM.ravel()
df_data_NM = df_data.as_matrix()

###
### Generate Training and Testing Set 
###
X_train, X_test, y_train, y_test = cross_validation.train_test_split(df_data_NM, df_label_NM_1D,test_size=0.10, random_state=42)


###
### Define Classifier
###                             
rf = RandomForestClassifier(n_estimators=100) # initialize

###
### Train Classifier 
###                              
rf.fit(X_train, y_train) # fit the data to the algorithm

###
### Print Accuracy and Confusion Matrix
###

output = rf.predict(X_test)

from sklearn.metrics import confusion_matrix
matrix = confusion_matrix(output, y_test)
score = rf.score(X_test, y_test)
#print "accuracy: {0}".format(score.mean())
print (score)
print (matrix)


###
### Save Classifier
###
from sklearn.externals import joblib
#Change file path to point to location of nb.pkl file.
joblib.dump(rf, 'C:/nb.pkl')