package com.nokia.ices.apps.fusion.common.connector.ssh2;

import java.io.File;
import java.io.IOException;

import ch.ethz.ssh2.KnownHosts;
import ch.ethz.ssh2.ServerHostKeyVerifier;

/**
 * This ServerHostKeyVerifier asks the user on how to proceed if a key cannot be found
 * in the in-memory database.
 * 
 * @author kongdy
 *
 */
public class AdvancedVerifier implements ServerHostKeyVerifier {

	private KnownHosts database;

	private String knownHostPath;

	private String tipMessage = "";

	public AdvancedVerifier(KnownHosts database, String knownHostPath) {
		this.database = database;
		this.knownHostPath = knownHostPath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.ethz.ssh2.ServerHostKeyVerifier#verifyServerHostKey(java.lang.String, int, java.lang.String, byte[])
	 */
	@Override
	public boolean verifyServerHostKey(String hostname, int port, String serverHostKeyAlgorithm, byte[] serverHostKey)
			throws Exception {
		final String host = hostname;
		final String algo = serverHostKeyAlgorithm;

		/* Check database */

		int result = database.verifyHostkey(hostname, serverHostKeyAlgorithm, serverHostKey);

		switch (result) {
		case KnownHosts.HOSTKEY_IS_OK:
			return true;

		case KnownHosts.HOSTKEY_IS_NEW:
			tipMessage = "Do you want to accept the hostkey (type " + algo + ") from " + host + " ?\n";
			break;

		case KnownHosts.HOSTKEY_HAS_CHANGED:
			tipMessage = "WARNING! Hostkey for " + host + " has changed!\nAccept anyway?\n";
			break;

		default:
			throw new IllegalStateException();
		}

		/* Include the fingerprints in the message */

		String hexFingerprint = KnownHosts.createHexFingerprint(serverHostKeyAlgorithm, serverHostKey);
		String bubblebabbleFingerprint = KnownHosts
				.createBubblebabbleFingerprint(serverHostKeyAlgorithm, serverHostKey);

		tipMessage += "Hex Fingerprint: " + hexFingerprint + "\nBubblebabble Fingerprint: " + bubblebabbleFingerprint;

		/* Be really paranoid. We use a hashed hostname entry */

		String hashedHostname = KnownHosts.createHashedHostname(hostname);

		/* Add the hostkey to the in-memory database */

		database.addHostkey(new String[] { hashedHostname }, serverHostKeyAlgorithm, serverHostKey);

		/* Also try to add the key to a known_host file */

		try {
			KnownHosts.addHostkeyToFile(new File(knownHostPath), new String[] { hashedHostname },
					serverHostKeyAlgorithm, serverHostKey);
		} catch (IOException ignore) {
		}

		return true;
	}

	public String getTipMessage() {
		return tipMessage;
	}

}
