
# Import
import tensorflow as tf
import csv
import numpy as np
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
import matplotlib.pyplot as plt
import sys
import urllib.request
from firebase import firebase

filename = "AAPL.csv"

dates = []
price = []

firebase = firebase.FirebaseApplication('https://stockexpertapp.firebaseio.com')
url = firebase.get('/URL', None)

print(url + "\n")

response = urllib.request.urlopen(url)

with open(filename, 'r') as response:
    csvFileReader = csv.reader(response)
    next(csvFileReader)

    i = 0
    for row in csvFileReader:
        for a in range(len(row)):
            d = ((row[0].split('-')))
            date =[]
            for i in d:
                date.append(int(i))
            dates.append(date)
        price.append(float(row[2]))

print("the script has the name %s" % (url))
#print(dates)
#print(price)

data = []
data.append(dates)
data.append(price)

X_train = np.array(dates[0:8000])
y_train = np.array(price[0:8000])

X_test = dates[8001:9000]
y_test = price[8001:9000]

n_stocks = 8000

print(X_train.shape)

# Brain Cells
n_neurons_1 = 1024
n_neurons_2 = 512
n_neurons_3 = 256
n_neurons_4 = 128

net = tf.InteractiveSession()

# Placeholders
X = tf.placeholder(dtype=tf.float32, shape=[None, 3])
Y = tf.placeholder(dtype=tf.float32, shape=[None])

# Initializers
sigma = 1
weight_initializer = tf.variance_scaling_initializer(mode="fan_avg", distribution="uniform", scale=sigma)
bias_initializer = tf.zeros_initializer()

# Weights
W_hidden_1 = tf.Variable(weight_initializer([3, n_neurons_1]))
W_hidden_2 = tf.Variable(weight_initializer([n_neurons_1, n_neurons_2]))
W_hidden_3 = tf.Variable(weight_initializer([n_neurons_2, n_neurons_3]))
W_hidden_4 = tf.Variable(weight_initializer([n_neurons_3, n_neurons_4]))

#Biases
bias_hidden_1 = tf.Variable(bias_initializer([n_neurons_1]))
bias_hidden_2 = tf.Variable(bias_initializer([n_neurons_2]))
bias_hidden_3 = tf.Variable(bias_initializer([n_neurons_3]))
bias_hidden_4 = tf.Variable(bias_initializer([n_neurons_4]))

# Output weights
W_out = tf.Variable(weight_initializer([n_neurons_4, 1]))
bias_out = tf.Variable(bias_initializer([1]))

# Hidden layer
hidden_1 = tf.nn.relu(tf.add(tf.matmul(X, W_hidden_1), bias_hidden_1))
hidden_2 = tf.nn.relu(tf.add(tf.matmul(hidden_1, W_hidden_2), bias_hidden_2))
hidden_3 = tf.nn.relu(tf.add(tf.matmul(hidden_2, W_hidden_3), bias_hidden_3))
hidden_4 = tf.nn.relu(tf.add(tf.matmul(hidden_3, W_hidden_4), bias_hidden_4))

out = tf.transpose(tf.add(tf.matmul(hidden_4, W_out), bias_out))
print(tf.transpose(tf.add(tf.matmul(hidden_4, W_out), bias_out)))

mse = tf.reduce_mean(tf.squared_difference(out, Y))
print(tf.reduce_mean(tf.squared_difference(out, Y)))

opt = tf.train.AdamOptimizer().minimize(mse)

net.run(tf.global_variables_initializer())

# Graphs
plt.ion()
fig = plt.figure()
ax1 = fig.add_subplot(111)
line1, = ax1.plot(y_test)
#line2, = ax1.plot(y_test * 0.5)
plt.show()

# Fit neural net
batch_size = 256
mse_train = []
mse_test = []

# Run
epochs = 50
for e in range(epochs):


    for i in range(0, len(y_train) // batch_size):
        start = i * batch_size
        batch_x = X_train[start:start + batch_size]
        batch_y = y_train[start:start + batch_size]

        net.run(opt, feed_dict={X: batch_x, Y: batch_y})



            #Training AI
        mse_train.append(net.run(mse, feed_dict={X: batch_x, Y: batch_y}))
#         print('MSE Train: ', mse_train[-1])
        #Testing AI
        mse_test.append(net.run(mse, feed_dict={X: X_test, Y: y_test}))
#         print('MSE Test: ', mse_test[-1])






#        plt.title('Epoch ' + str(e) + ', Batch ' + str(i))
#        plt.pause(0.01)




# PREDICTION
print(net.run(out, feed_dict={X: [[2019,3,25]]}))
