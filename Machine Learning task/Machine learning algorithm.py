import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import confusion_matrix,classification_report
from sklearn.preprocessing import StandardScaler,LabelEncoder
from sklearn.model_selection import train_test_split

# reference: "Machine Learning With Scikit-Learn"
# link: https://www.youtube.com/watch?v=0Lt9w-BxKFQ

cholesterol = pd.read_csv("report.csv", sep=',')
print(cholesterol)

# seperate to two level of group, below cholesterol value below 200 is average while above 200 is high
bins = (2,200,3000)
group_names = ['average','high']
# cutting cholesterol and replacing it
cholesterol['cholesterol'] = pd.cut(cholesterol["cholesterol"], bins= bins, labels = group_names)
cholesterol['cholesterol'].unique()

# encode label "average" to 0 and "high" to 1
label_cholesterol = LabelEncoder()
cholesterol['cholesterol'] = label_cholesterol.fit_transform(cholesterol['cholesterol'])

# separate the dataset as response variable and feature variable
X = cholesterol.drop('cholesterol', axis = 1) # drop cholesterol
y = cholesterol['cholesterol']

# Train and test splitting of data
X_train, X_test, y_train,y_test= train_test_split(X,y,test_size=0.2, random_state=42)

# scale date to get optimised result
sc = StandardScaler()
X_train =sc.fit_transform(X_train)
X_test = sc.transform(X_test)

rfc = RandomForestClassifier(n_estimators=300)
rfc.fit(X_train,y_train)
pred_rfc = rfc.predict(X_test)
print(classification_report(y_test,pred_rfc))
print(confusion_matrix(y_test,pred_rfc))
