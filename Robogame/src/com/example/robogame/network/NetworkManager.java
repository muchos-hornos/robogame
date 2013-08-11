package com.example.robogame.network;

import java.io.IOException;
import java.net.ServerSocket;

import android.annotation.TargetApi;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.RegistrationListener;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.util.Log;

// TODO: Use wifi direct for networking.
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class NetworkManager {
	public static final String SERVICE_NAME = "NsdGame";
	public static final String SERVICE_TYPE = "_http._tcp.";
	

	public void registerService(int port) {
		// Create the NsdServiceInfo object, and populate it.
		NsdServiceInfo serviceInfo  = new NsdServiceInfo();

		// The name is subject to change based on conflicts
		// with other services advertised on the same network.
		serviceInfo.setServiceName(SERVICE_NAME);
		serviceInfo.setServiceType(SERVICE_TYPE);
		serviceInfo.setPort(port);
		
		// TODO: pass context.
		//mNsdManager = Context.getSystemService(Context.NSD_SERVICE);

	    mNsdManager.registerService(
	            serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
	}

	public void initializeServerSocket() throws IOException {
		// Initialize a server socket on the next available port.
		mServerSocket = new ServerSocket(0);

		// Store the chosen port.
		mLocalPort =  mServerSocket.getLocalPort();
	}
	
	public void initializeRegistrationListener() {
	    mRegistrationListener = new RegistrationListener() {

	        @Override
	        public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
	            // Save the service name.  Android may have changed it in order to
	            // resolve a conflict, so update the name you initially requested
	            // with the name Android actually used.
	            mServiceName = NsdServiceInfo.getServiceName();
	            Log.d("RoboNW", "Registered service with name " + mServiceName);
	        }

	        @Override
	        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
	            // Registration failed!  Put debugging code here to determine why.
	        	Log.d("RoboNW", "Registration failed errorCode: " + errorCode);
	        }

	        @Override
	        public void onServiceUnregistered(NsdServiceInfo arg0) {
	            // Service has been unregistered.  This only happens when you call
	            // NsdManager.unregisterService() and pass in this listener.
	        	Log.d("RoboNW", "Service unregistered name: " + arg0.getServiceName());
	        }

	        @Override
	        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
	            // Unregistration failed.  Put debugging code here to determine why.
	        	Log.d("RoboNW", "Unegistration failed errorCode: " + errorCode);
	        }
	    };
	}

	private ServerSocket mServerSocket;
	private int mLocalPort;
	private NsdManager mNsdManager;
	
	private String mServiceName;
	private RegistrationListener mRegistrationListener;
}
