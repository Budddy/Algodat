
package ads1ss12.pa;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

/**
 * SecurityManager f&uuml;r das ADS1 Framework.
 * 
 * <p>
 * <b>WICHTIG:</b> Nehmen Sie keine &Auml;nderungen in dieser Klasse vor. Bei der Abgabe werden diese &Auml;nderungen
 * verworfen und es k&ouml;nnte dadurch passieren, dass Ihr Programm somit nicht mehr korrekt funktioniert.
 * </p>
 */
public class ADS1SecurityManager extends SecurityManager {
	
	static private Class<? extends Object>	userclass	= AvlTree.class;
	
	/**
	 * Erzeugt einen neuen SecurityManager
	 * 
	 */
	public ADS1SecurityManager() {
	
		super();
	}
	
	@Override
	public void checkAccept(String arg0, int arg1) {
	
		this.checkOrigin("ServerSocket.accept()");
	}
	
	@Override
	public void checkAccess(Thread arg0) {
	
		this.checkOrigin("Thread access");
	}
	
	@Override
	public void checkAccess(ThreadGroup arg0) {
	
		this.checkOrigin("ThreadGroup access");
	}
	
	@Override
	public void checkConnect(String arg0, int arg1) {
	
		this.checkOrigin("Socket.connect()");
	}
	
	@Override
	public void checkConnect(String arg0, int arg1, Object arg2) {
	
		this.checkOrigin("Socket.connect()");
	}
	
	@Override
	public void checkCreateClassLoader() {
	
		this.checkOrigin("Create ClassLoader");
	}
	
	@Override
	public void checkDelete(String arg0) {
	
		this.checkOrigin("File.delete()");
	}
	
	@Override
	public void checkExec(String arg0) {
	
		this.checkOrigin("Runtime.exec()");
	}
	
	@Override
	public void checkExit(int status) {
	
		this.checkOrigin("System.exit()");
	}
	
	@Override
	public void checkLink(String arg0) {
	
		this.checkOrigin("Runtime.load()/loadLibrary()");
	}
	
	@Override
	public void checkListen(int arg0) {
	
		this.checkOrigin("Listen to port");
	}
	
	@Override
	public void checkMulticast(InetAddress arg0) {
	
		this.checkOrigin("Multicast");
	}
	
	private void checkOrigin(String type) throws SecurityException {
	
		for (Object x : this.getClassContext()) {
			if (x.equals(ADS1SecurityManager.userclass)) {
				throw new SecurityException(type + ": " + x);
			}
			else
				if (x.equals(Main.class)) {
					return;
				}
		}
		
		throw new SecurityException(type);
	}
	
	@Override
	public void checkPermission(Permission perm) {
	
		for (Object x : this.getClassContext()) {
			if (x.equals(ADS1SecurityManager.userclass)) {
				throw new SecurityException("setSecurityManager: " + x);
			}
			else
				if (x.equals(Main.class)) {
					return;
				}
		}
		super.checkPermission(perm);
	}
	
	@Override
	public void checkPrintJobAccess() {
	
		this.checkOrigin("Print");
	}
	
	@Override
	public void checkPropertiesAccess() {
	
		this.checkOrigin("System.set/getProperties()");
	}
	
	@Override
	public void checkPropertyAccess(String arg0) {
	
		this.checkOrigin("System.getProperty(" + arg0 + ")");
	}
	
	@Override
	public void checkRead(FileDescriptor arg0) {
	
	}
	
	@Override
	public void checkRead(String arg0) {
	
	}
	
	@Override
	public void checkRead(String arg0, Object arg1) {
	
	}
	
	@Override
	public void checkSetFactory() {
	
		this.checkOrigin("Set Socket/ServerSocket/URL Factory");
	}
	
	@Override
	public void checkSystemClipboardAccess() {
	
		this.checkOrigin("SystemClipboard access");
	}
	
	@Override
	public void checkWrite(FileDescriptor arg0) {
	
		this.checkOrigin("Write FileDescriptor");
	}
	
	@Override
	public void checkWrite(String arg0) {
	
		this.checkOrigin("Write File");
	}
	
}
