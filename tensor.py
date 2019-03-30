import csv
import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt

import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

filename = "AAPL.csv"

dates = []
highPrice = []

def get_data(filename):
    with open(filename, 'r') as csvfile:
        csvFileReader = csv.reader(csvfile)
        print(csv.reader(csvfile))
        next(csvFileReader)

        i = 0
        for row in csvFileReader:

            dates.append(row[0].split('-'))
            highPrice.append(float(row[2]))

            i = i + 1

    return dates, highPrice

get_data(filename)
placeholder1 = tf.placeholder(tf.float32, shape=[None, 3])


#trainers
dates_train = np.array(dates[0:8000]).astype(np.float32)
highPrice_train = np.array(highPrice[0:8000]).astype(np.float32)

#testers
dates_test = np.array(dates[8000:9564]).astype(np.float32)
highPrice_test = np.array(highPrice[8000:9564]).astype(np.float32)

def get_training_batch(n):
    n = min(n,7999)
    idx = np.random.choice(7999,n)
    return dates_train[idx],highPrice_train[idx]

n_hidden_1 = 100
n_hidden_2 = 100

weights = {
'h1' : tf.Variable(tf.random_normal([3, n_hidden_1])),
'h2' : tf.Variable(tf.random_normal([n_hidden_1,n_hidden_2])),
'out' : tf.Variable(tf.random_normal([n_hidden_2,1]))
}

biases = {
'b1' : tf.Variable(tf.random_normal([n_hidden_1])),
'b2' : tf.Variable(tf.random_normal([n_hidden_2])),
'out' : tf.Variable(tf.random_normal([1]))
}

layer_1 = tf.nn.sigmoid(tf.add(tf.matmul(placeholder1, weights['h1']),biases['b1']))
layer_2 = tf.nn.sigmoid(tf.add(tf.matmul(layer_1, weights['h2']), biases['b2']))

y = tf.matmul(layer_2,weights['out']) + biases['out']

placeholder2 = tf.placeholder(tf.float32,shape=[None,1])
cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits_v2(logits=y, labels=placeholder2))

rate = 0.01
optimizer = tf.train.GradientDescentOptimizer(rate).minimize(cross_entropy)

prediction = tf.nn.softmax(y)

##Training

correct_prediction = tf.equal(tf.argmax(prediction, 1), tf.argmax(placeholder2,1))
accuracy = 100 * tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

epochs = 1000
batch_size = 10

sess = tf.InteractiveSession()

sess.run(tf.global_variables_initializer())

cost = []
accu = []
test_accu = []
for ep in range(epochs):
    x_feed,y_feed = get_training_batch(batch_size)
    y_feed = np.reshape(y_feed,[10,1])
    _,cos,predictions,acc = sess.run([optimizer, cross_entropy, prediction, accuracy], feed_dict={placeholder1:x_feed, placeholder2:y_feed})

    test_acc = accuracy.eval(feed_dict={placeholder1:x_feed, placeholder2:y_feed})
    cost.append(cos)
    accu.append(acc)
    test_accu.append(test_acc)

    if(ep % (epochs // 10) == 0):
        print('[%d]: Cos: %.4f, Acc: %.1f%%, Test Acc: %.1f%%' % (ep,cos,acc,test_acc))

plt.plot(cost)
plt.title('cost')
plt.show()

plt.plot(accu)
plt.title('Train Accuracy')
plt.show()

plt.plot(test_accu)
plt.title('Test Accuracy')
plt.show()

index = 36
p = sess.run(prediction, feed_dict={placeholder1:dates_train[index:index +1]})[0]
