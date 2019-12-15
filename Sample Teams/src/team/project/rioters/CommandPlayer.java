package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.Selector;
import behavior_tree.Sequence;
import bt_team.player.BTreePlayer;
import easy_soccer_lib.PlayerCommander;
import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.MatchPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;

public class CommandPlayer extends Thread {
	
	final PlayerCommander commander;
	
	PlayerPerception selfPerc;
	FieldPerception  fieldPerc;
	MatchPerception matchPerc;
	
	Vector2D homePosition;
	Vector2D goalPosition;
	
	BTNode<BTreePlayer> btree;
	
	
	public CommandPlayer(PlayerCommander player) {
		commander = player;
	}
	
	@Override
	public void run() {
		
	}
	
	private void updatePerceptions() {
		PlayerPerception newSelf = commander.perceiveSelfBlocking();
		FieldPerception newField = commander.perceiveFieldBlocking();
		MatchPerception newMatch = commander.perceiveMatchBlocking();
		if (newSelf != null)
			this.selfPerc = newSelf;
		if (newField != null)
			this.fieldPerc = newField;
		if (newMatch != null)
			this.matchPerc = newMatch;
	}
}
