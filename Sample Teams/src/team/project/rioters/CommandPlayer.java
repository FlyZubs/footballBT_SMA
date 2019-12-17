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
	FieldPerception fieldPerc;
	MatchPerception matchPerc;

	Vector2D homePosition;
	Vector2D goalPosition;

	BTNode<CommandPlayer> btree;

	public CommandPlayer(PlayerCommander player, Vector2D home) {
		commander = player;
		homePosition = home;

		btree = mainTree();
	}
	
	private BTNode<CommandPlayer> goalKickTree() {
		Sequence<CommandPlayer> raiz = new Sequence<CommandPlayer>("RAIZ GOAL KICK");
		
		
		
		return raiz;
	}

	private BTNode<CommandPlayer> mainTree() {
		Selector<CommandPlayer> raiz = new Selector<CommandPlayer>("RAIZ");

		// all team go to initial position on BEFORE KICK OFF
		Sequence<CommandPlayer> seqBeforeKickOff = new Sequence<CommandPlayer>("BEFORE KICK OFF");
		seqBeforeKickOff.add(new IsBeforeKickOff());
		seqBeforeKickOff.add(new MoveToHome());
		
		// all team go to initial position on KICK OFF
		Sequence<CommandPlayer> seqKickOff = new Sequence<CommandPlayer>("KICK OFF");
		seqKickOff.add(new IsKickOff());
		seqKickOff.add(new GoToHome());
		
		// 
		Sequence<CommandPlayer> seqGoalKickA = new Sequence<CommandPlayer>("GOAL KICK A");
		seqGoalKickA.add(new IsGoalKick());
		Selector<CommandPlayer> selGoalKickA = new Selector<CommandPlayer>("GOAL KICK A");
		Sequence<CommandPlayer> seqGoalKickB = new Sequence<CommandPlayer>("GOAL KICK B");
		seqGoalKickB.add(new IsMyTeam());
		Selector<CommandPlayer> selGoalKickB = new Selector<CommandPlayer>("GOAL KICK B");
		Sequence<CommandPlayer> seqGoalKickC = new Sequence<CommandPlayer>("GOAL KICK C");
		seqGoalKickC.add(new IsGoalie());
		seqGoalKickC.add(new DoGoalKick());
		selGoalKickB.add(seqGoalKickC);
		selGoalKickB.add(new DefenseBehavior());
		seqGoalKickB.add(selGoalKickB);
		selGoalKickA.add(seqGoalKickB);
		selGoalKickA.add(new AttackBehavior());
		seqGoalKickA.add(selGoalKickA);
		
		//
		Sequence<CommandPlayer> seqKickIn = new Sequence<CommandPlayer>("KICK IN");
		seqKickIn.add(new IsKickIn());
		
		//
		Sequence<CommandPlayer> seqOffside = new Sequence<CommandPlayer>("OFFSIDE");
		seqOffside.add(new IsOffside());
		
		//
		Sequence<CommandPlayer> seqBackPass = new Sequence<CommandPlayer>("BACK PASS");
		seqBackPass.add(new IsBackPass());
		
		//
		Sequence<CommandPlayer> seqIndirectFreeKick = new Sequence<CommandPlayer>("INDIRECT FREE KICK");
		seqIndirectFreeKick.add(new IsIndirectFreeKick());
				
		//
		Sequence<CommandPlayer> seqFreeKick = new Sequence<CommandPlayer>("FREE KICK");
		seqFreeKick.add(new IsFreeKick());
		
		//
		Sequence<CommandPlayer> seqCornerKick = new Sequence<CommandPlayer>("CORNER KICK");
		seqCornerKick.add(new IsCornerKick());
		
		//
		Sequence<CommandPlayer> seqFreeKickFault = new Sequence<CommandPlayer>("FREE KICK FAULT");
		seqFreeKickFault.add(new IsFreeKickFault());

		// check if the player is goal keeper
		Sequence<CommandPlayer> seqGoalKeeper = new Sequence<CommandPlayer>("GOAL KEEPER");
		seqGoalKeeper.add(new IsGoalKeeper());

		// player behavior depends on ball possession
		Selector<CommandPlayer> selBallPossession = new Selector<CommandPlayer>("BALL POSSESSION");

		// check if the player has the ball possession
		Sequence<CommandPlayer> seqBallPossession = new Sequence<CommandPlayer>("BALL POSSESSION");
		seqBallPossession.add(new BallPossession());

		// if the player doesn't has ball possession it will do an offensive or
		// defensive behavior
		Selector<CommandPlayer> selBehavior = new Selector<CommandPlayer>("ATTACK OR DEFEND");

		// check if the team has the ball possession
		Sequence<CommandPlayer> seqAttack = new Sequence<CommandPlayer>("ATTACK");
		seqAttack.add(new TeamBallPossession());

		// do this if the team don't have the ball possession
		// Sequence<CommandPlayer> seqDefense = new Sequence<CommandPlayer>("DEFENSE");
		// do stuff

		

		return raiz;
	}

	@Override
	public void run() {
		System.out.println(">> Executando...");
		updatePerceptions();

		if (selfPerc.getSide() == EFieldSide.LEFT) {
			goalPosition = new Vector2D(52.0d, 0);
		} else {
			goalPosition = new Vector2D(-52.0d, 0);
			homePosition.setX(-getHomePosition().getX());
		}
		commander.doMoveBlocking(homePosition);
		updatePerceptions();

		while (commander.isActive()) {

			btree.tick(this);

			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			updatePerceptions(); // non-blocking
		}

		System.out.println(">> Fim da execução!");
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

	public PlayerCommander getCommander() {
		return commander;
	}

	public MatchPerception getMatchPerception() {
		return matchPerc;
	}

	public Vector2D getHomePosition() {
		return homePosition;
	}
	
	public EFieldSide getSide() {
		return selfPerc.getSide();
	}
	
	public Vector2D getBallPos() {
		return fieldPerc.getBall().getPosition();
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
