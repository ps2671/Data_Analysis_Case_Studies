
# coding: utf-8

# In[ ]:


import pandas as pd
import numpy as np
import km
from sklearn import ensemble
from sklearn.cluster import DBSCAN
import json


df = pd.read_csv("nfr_tf-idf_matrix.csv")
feature_names = [c for c in df.columns if c not in ["class"]]
X = np.array(df[feature_names].fillna(0)) # quick and dirty imputation
y = np.array(df["class"])

# We create a custom 1-D lens with Isolation Forest
model = ensemble.IsolationForest(random_state=1729)
model.fit(X)
lens1 = model.decision_function(X).reshape((X.shape[0], 1))

# We create another 1-D lens with L2-norm
mapper = km.KeplerMapper(verbose=3)
#lens2 = mapper.fit_transform(X, projection=km.manifold.TSNE())
lens2 = mapper.fit_transform(X, projection="l2-norm")

# Combine both lenses to create a 2-D [Isolation Forest, L^2-Norm] lens
lens = np.c_[lens2]

'''
# Create the simplicial complex
graph = mapper.map(lens, 
                   X, 
                   nr_cubes=15, 
                   overlap_perc=0.7, 
                   clusterer=km.cluster.KMeans(n_clusters=2, 
                                         random_state=1618033))
'''
# Create the simplicial complex
graph = mapper.map(lens, 
                   X, 
                   nr_cubes=25, 
                   overlap_perc=0.7, 
                   clusterer=DBSCAN(eps=0.3, min_samples=3))


print(graph)

with open('data.json', 'w') as fp:
    json.dump(graph, fp)
    
# Visualization
mapper.visualize(graph, 
                 path_html="nfr_tfidf_TDA.html", 
                 title="nfr_tfidf_TDA Dataset", 
                 custom_tooltips=y)


# In[ ]:





# In[ ]:




