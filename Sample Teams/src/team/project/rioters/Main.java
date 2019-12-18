package team.project.rioters;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args) {
		try {
			CommandTeam teamA = new CommandTeam("_RiotersBT");
			CommandTeam teamB = new CommandTeam("B");
			teamA.launchTeamAndServer();
			teamB.launchTeam();
		} catch (UnknownHostException e) {
			System.out.println("Falha ao conectar.");
		}
	}

}
