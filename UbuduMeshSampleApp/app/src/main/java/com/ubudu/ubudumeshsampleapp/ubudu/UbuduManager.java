package com.ubudu.ubudumeshsampleapp.ubudu;

import android.content.Context;
import android.widget.Toast;

import com.ubudu.mesh.UbuduMeshDelegate;
import com.ubudu.mesh.UbuduMeshManager;
import com.ubudu.mesh.UbuduMeshNode;
import com.ubudu.mesh.UbuduMeshSDK;

import java.util.ArrayList;

/**
 * Created by mgasztold on 14/01/16.
 */
public class UbuduManager {

    public static final String TAG = "UbuduManager";

    private UbuduMeshManager mMeshManager;

    //UbuduManager listener
    private UbuduManagerListener managerListener;

    // Delegate
    private MyUbuduMeshDelegate delegate;

    // started flag
    private boolean started = false;

    // Context:
    private Context mContext;

    // Singleton instance
    private static UbuduManager client;

    public static synchronized UbuduManager getInstance(Context ctx, UbuduManagerListener listener){
        if(client==null) {
            synchronized (UbuduManager.class) {
                if(client==null) {
                    client=new UbuduManager(ctx, listener);
                }
            }
        }
        return client;
    }

    public static UbuduManager getSharedInstance(){
        return client;
    }

    private UbuduManager(Context ctx, UbuduManagerListener listener) {
        mContext = ctx;
        managerListener = listener;

        delegate = new MyUbuduMeshDelegate();

        mMeshManager = UbuduMeshSDK.getInstance().getMeshManager(mContext);
        mMeshManager.setMeshDelegate(delegate);
    }

    public boolean isMeshManagerStarted(){
        return started;
    }

    public void startMeshManager(){
        mMeshManager.start();
    }

    public void stopMeshManager(){
        mMeshManager.stop();
    }

    public void sendMeshMessage(String meshMessage, int meshId, String networkUUID, boolean shouldDisconnectAfterSending, boolean shouldWaitForAck) {
        mMeshManager.sendMeshMessage(meshMessage,meshId,networkUUID,shouldDisconnectAfterSending,shouldWaitForAck);
    }

    private class MyUbuduMeshDelegate implements UbuduMeshDelegate{

        @Override
        public void onSendMeshMessage(int status) {
            Toast.makeText(mContext, String.format("onSendMeshMessage: %d", status), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpdateVisibleAndConnectableNodes(ArrayList<UbuduMeshNode> nodes) {
            android.util.Log.d(TAG, String.format("onUpdateVisibleAndConnectableNodes: %s", nodes.toString()));
        }

        @Override
        public void onReceiveMeshMessage(byte[] bytes, byte[] bytes1) {

        }

        @Override
        public void startSucceed() {
            started = true;
            Toast.makeText(mContext, "startSucceed mesh", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void startFailed() {
            started = false;
            Toast.makeText(mContext, "startFailed mesh", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void stopSucceed() {
            started = false;
            Toast.makeText(mContext, "stopSucceed mesh", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void stopFailed() {
            Toast.makeText(mContext, "stopFailed mesh", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void meshIsConnectingToNode(UbuduMeshNode ubuduMeshNode) {
            Toast.makeText(mContext, String.format("meshIsConnectingToNode"), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void meshConnectionSucceed(UbuduMeshNode ubuduMeshNode) {
            Toast.makeText(mContext, String.format("meshConnectionSucceed"), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void meshIsDisconnecting() {
            Toast.makeText(mContext, String.format("meshIsDisconnecting"), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void meshDisconnected() {
            Toast.makeText(mContext, String.format("meshDisconnected"), Toast.LENGTH_SHORT).show();
        }
    }
}
