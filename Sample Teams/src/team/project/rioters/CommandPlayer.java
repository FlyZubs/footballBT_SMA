package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.Selector;
import behavior_tree.Sequence;

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
	
	BTNode<CommandPlayer> btree;
	
	
	public CommandPlayer(PlayerCommander player, Vector2D home) {
		commander = player;
		homePosition = home;
	}

	private BTNode<CommandPlayer> buildTree() {
		Selector<CommandPlayer> raiz = new Selector<CommandPlayer>("RAIZ");
		
		// all team go to initial position on KICK OFF
		Sequence<CommandPlayer> seqKickOff = new Sequence<CommandPlayer>("KICK OFF");
		seqKickOff.add(new IsKickOff());
		seqKickOff.add(new GoToHome());
		
		// check if the player is goal keeper
		Sequence<CommandPlayer> seqGoalKeeper = new Sequence<CommandPlayer>("GOAL KEEPER");
		//seqGoalKeeper.add(new IsGoalKeeper());
		// do stuff
		
		// player behavior depends on ball possession
		Selector<CommandPlayer> selA = new Selector<CommandPlayer>("BALL POSSESSION");
		
		// check if the player has the ball possession
		Sequence<CommandPlayer> seqBallPossession = new Sequence<CommandPlayer>("BALL POSSESSION");
		//seqBallPossession.add(new BallPossession());
		// do stuff
		
		// if the player doesn't has ball possession it will do an offensive or defensive behavior
		Selector<CommandPlayer> selB = new Selector<CommandPlayer>("ATTACK OR DEFEND");
		
		// check if the team has the ball possession
		Sequence<CommandPlayer> seqAttack = new Sequence<CommandPlayer>("ATTACK");
		//seqAttack.add(new TeamBallPossession());
		// do stuff
		
		// do this if the team don't have the ball possession
		//Sequence<CommandPlayer> seqDefense = new Sequence<CommandPlayer>("DEFENSE");
		// do stuff
		
		selB.add(seqAttack);
		
		selA.add(seqBallPossession);
		selA.add(selB);
		
		raiz.add(seqKickOff);
		raiz.add(seqGoalKeeper);
		raiz.add(selA);
		
		return raiz;
	}
	
	@Override
	public void run() {
		System.out.println(">> Executando...");
		updatePerceptions();
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
	
	public MatchPerception getMatchPerception() {
		return matchPerc;
	}
	
	public Vector2D getHomePosition() {
		return homePosition;
	}
	
	public void turnToPoint(Vector2D point) {
		Vector2D newDirection = point.sub(selfPerc.getPosition());
		commander.doTurnToDirection(newDirection);
	}

	public boolean isAlignToPoint(Vector2D point, double margin) {
		double angle = point.sub(selfPerc.getPosition()).angleFrom(selfPerc.getDirection());
		return angle < margin && angle > margin * (-1);
	}
	
	public void dash(Vector2D point, int speed) {
		if (selfPerc.getPosition().distanceTo(point) <= 1)
			return;
		if (!isAlignToPoint(point, 30))
			turnToPoint(point);
		// commander.doMove(x, y)
		commander.doDash(speed);
	}
	
	public boolean isPointsAreClose(Vector2D reference, Vector2D point, double margin) {
		return reference.distanceTo(point) <= margin;
	}
}
