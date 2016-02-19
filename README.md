# Ubudu Android Mesh SDK v1.1.0

## Ubudu Mesh SDK for Android

Ubudu's mesh technology enables regular mobile devices (like smartphones, tablets) to exchange messages with devices which are not in direct proximity and would not be able to connect otherwise. You can find the java docs of the API here : [http://www.ubudu.com/docs/android/mesh_sdk/index.html](http://www.ubudu.com/docs/android/mesh_sdk/index.html)

## System and hardware requirements
Any Android device with Bluetooth 4.0 and Android 4.3 or higher.

## Adding MESH to mobile application

### Adding Mesh SDK

To use the library in an Android Studio project simply add:

compile('com.ubudu.indoorlocation:ubudu-mesh-sdk:1.0.1@aar') {
    transitive = true }
to your project dependencies and run gradle build.

A jar file of the SDK is also available in the /Ubudu-Mesh-SDK directory of this repository. To use it in your project (e.g. in Eclipse IDE) drop the jar file into the libs folder and configure the Java build path to include the library.

### Usage instructions

The UbuduSDK instance provides method to obtain the mesh manager.

`UbuduMeshManager meshManager = UbuduMeshSDK.getInstance().getMeshManager(this);`

#### Start / Stop mesh manager
To start using mesh `UbuduMeshManager` must be started. It may be done by calling:
`meshManager.start()`.

`start()`  method starts searching for connectable, mesh enabled beacons.

To stop mesh manager and prevent searching for mesh beacons call `stop()` method.

#### Sending message through mesh
Mesh is super easy to use. Just call:

`public void sendMeshMessage(String meshMessage, Integer meshId, String networkUUID);`

- **MeshMessage** is a string with mesh message to be send. It should be no longer than 16 bytes.

- **meshId** is the device address in dec format. Should be between 1-32767.

- **networkUUID** String with UUID of the mesh network UUID. ```meshId``` should be part of the network.

Note that this method will connect to the nearest connectable and mesh enabled beacon end then send message with ACK and disconnects automatically. There is also a possibility to send message without confirmation and without auto-disconnecting. It is useful if some response is expected. Please check the API doc.

#### Receiving status of the message
`UbuduMeshDelegate` interface must be implemented to get feedback from mesh manager. At the moment receiving status is possible by method: `public void onSendMeshMessage(int status);`. This method is called by mesh manager after sending a message. status values can be:

```
MESH_MESSAGE_STATUS_OK = 0;				// Message sent with success
MESH_MESSAGE_STATUS_FAIL = 1;			// Sending message failed - other reason
MESH_MESSAGE_STATUS_FAIL_TIMEOUT = 2;	// Sending message failed because of timeout
MESH_MESSAGE_STATUS_FAIL_NO_MESH = 3;	// Sending message failed because of missing Mesh service
MESH_MESSAGE_STATUS_NO_MESH_NODE = 4;	// Sending message failed because of no nearby mesh connectable node
MESH_MESSAGE_STATUS_TOO_LONG = 5;		// Sending message failed because of too long message
```

#### Receiving feedback about nearby mesh nodes
`UbuduMeshDelegate` allows you to be notified about number of nearby availble mesh connectable nodes by 
`public void onUpdateVisibleAndConnectableNodes(ArrayList<UbuduMeshNode>nodes);`.

#### Receiving mesh message on the device
To receive a message from a mesh network you have to be connected to the mesh node which is a receiver of the message. To handle message just implement this method:

```void onReceiveMeshMessage(byte[] meshIdData, byte[] meshMessageData);```

This method is called after receiveing mesh message from the connected node.