import tensorflow as tf
import keras.backend.tensorflow_backend as K
from tensorflow.python.framework import graph_util

sess = tf.Session()
K.set_session(sess)

model = build_model()
PRETRAINED_MODEL_PATH = 'x_train.h5'
model.load_weights(PRETRAINED_MODEL_PATH)

# We retrieve the protobuf graph definition
graph = tf.get_default_graph()
input_graph_def = graph.as_graph_def()
output_node_names = "Predictions"

# We use a built-in TF helper to export variables to constants
output_graph_def = graph_util.convert_variables_to_constants(
   sess, # The session is used to retrieve the weights
   input_graph_def, # The graph_def is used to retrieve the nodes,
   [n.name for n in input_graph_def.node]
)

# Finally we serialize and dump the output graph to the filesystem
with tf.gfile.GFile('graph.pb', "wb") as f:
    f.write(output_graph_def.SerializeToString())
print("%d ops in the final graph." % len(output_graph_def.node))