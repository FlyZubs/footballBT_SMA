package team.project.rioters;

import easy_soccer_lib.AbstractTeam;
import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.utils.Vector2D;

public class CommandTeam extends AbstractTeam {

	public CommandTeam(String prefix) {
		super("Team" + prefix, 8, false);
	}

	@Override
	protected void launchPlayer(int ag, PlayerCommander comm) {
		System.out.println("Player lançado! -> " + ag);
		double x, y;

		switch (ag) {
		case 0:
			x = -51.0d;
			y = 0.0d;
			break;
		case 1:
			x = -30.0d;
			y = 8.0d;
			break;
		case 2:
			x = -30.0d;
			y = -8.0d;
			break;
		case 3:
			x = -15.0d;
			y = 20.0d;
			break;
		case 4:
			x = -15.0d;
			y = 0.0d;
			break;
		case 5:
			x = -15.0d;
			y = -20.0d;
			break;
		case 6:
			x = -7.0d;
			y = 8.0d;
			break;
		case 7:
			x = -7.0d;
			y = -8.0d;
			break;
		default:
			x = 0.0d;
			y = 0;
		}
		
		CommandPlayer p = new CommandPlayer(comm, new Vector2D(x,y));
		p.start();
	}

}
