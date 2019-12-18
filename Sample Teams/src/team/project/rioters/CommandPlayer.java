package team.project.rioters;

import java.util.ArrayList;

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

	ArrayList<PlayerPerception> myTeam;
	ArrayList<PlayerPerception> enemyTeam;

	PlayerPerception playerToPass;
	PlayerPerception closestEnPlayer;

	BTNode<CommandPlayer> btree;

	public CommandPlayer(PlayerCommander player, Vector2D home) {
		commander = player;
		homePosition = home;

		btree = mainTree();
	}

	private BTNode<CommandPlayer> mainTree() {
		Selector<CommandPlayer> raiz = new Selector<CommandPlayer>("RAIZ");

		// all team go to initial position on BEFORE KICK OFF and on AFTER GOAL
		Sequence<CommandPlayer> seqBeforeKickOff = new Sequence<CommandPlayer>("BEFORE KICK OFF");
		seqBeforeKickOff.add(new IsBeforeKickOff());
		seqBeforeKickOff.add(new MoveToHome());

		// almost team go to initial position on KICK OFF
		// player 1 do the kick off
		Sequence<CommandPlayer> seqKickOff = new Sequence<CommandPlayer>("KICK OFF");
		seqKickOff.add(new IsKickOff());
		Selector<CommandPlayer> selKickOff = new Selector<CommandPlayer>("KICK OFF");
		Sequence<CommandPlayer> seqKickOffMyTeam = new Sequence<CommandPlayer>("KICK OFF MY TEAM");
		seqKickOffMyTeam.add(new IsMyTeam());
		seqKickOffMyTeam.add(new DoKickOff());
		selKickOff.add(seqKickOffMyTeam);
		selKickOff.add(new GoToHome());
		seqKickOff.add(selKickOff);

		// almost team go to initial position on GOAL KICK
		// player 7 do the goal kick
		Sequence<CommandPlayer> seqGoalKick = new Sequence<CommandPlayer>("GOAL KICK");
		seqGoalKick.add(new IsGoalKick());
		Selector<CommandPlayer> selGoalKick = new Selector<CommandPlayer>("GOAL KICK");
		Sequence<CommandPlayer> seqGoalKickMyTeam = new Sequence<CommandPlayer>("GOAL KICK MY TEAM");
		seqGoalKickMyTeam.add(new IsMyTeam());
		seqGoalKickMyTeam.add(new DoGoalKick());
		selGoalKick.add(seqGoalKickMyTeam);
		selGoalKick.add(new GoToHome());
		seqGoalKick.add(selGoalKick);

		// the two closest players to the ball will try to do the FREE KICK
		Sequence<CommandPlayer> seqFreeKick = new Sequence<CommandPlayer>("FREE KICK");
		seqFreeKick.add(new IsFreeKick());
		seqFreeKick.add(new IsMyTeam());
		seqFreeKick.add(new IsCloseToBall());
		seqFreeKick.add(new DoFreeKick());

		// the two closest players to the ball will try to do the CORNER KICK
		Sequence<CommandPlayer> seqCornerKick = new Sequence<CommandPlayer>("CORNER KICK");
		seqCornerKick.add(new IsCornerKick());
		seqCornerKick.add(new IsMyTeam());
		Selector<CommandPlayer> selCornerKick = new Selector<CommandPlayer>("CORNER KICK");
		Sequence<CommandPlayer> seqCornerKickPlayer = new Sequence<CommandPlayer>("CORNER KICK PLAYER");
		seqCornerKickPlayer.add(new IsCloseToBall());
		seqCornerKickPlayer.add(new DoCornerKick());
		selCornerKick.add(seqCornerKickPlayer);
		selCornerKick.add(new FollowInFrontOf());
		seqCornerKick.add(selCornerKick);

		// the two closest players to the ball will try to do the KICK IN, OFFSIDE,
		// BACKPASS or INDIRECT FREE KICK 
		Sequence<CommandPlayer> seqOtherState = new Sequence<CommandPlayer>("OTHER STATE");
		seqOtherState.add(new IsOtherState());
		seqOtherState.add(new IsMyTeam());
		seqOtherState.add(new IsCloseToBall());
		Selector<CommandPlayer> selOtherState = new Selector<CommandPlayer>("OTHER STATE");
		selOtherState.add(new GetClosestFreePlayer());
		selOtherState.add(new ChaseBall());
		seqOtherState.add(selOtherState);
		seqOtherState.add(new ChaseBall());
		seqOtherState.add(new PassBall());

		// check if the player is goal keeper
		Sequence<CommandPlayer> seqGoalKeeper = new Sequence<CommandPlayer>("GOAL KEEPER");
		seqGoalKeeper.add(new IsGoalKeeper());
		// goal keeper behavior depends on ball possession
		Selector<CommandPlayer> selGKBallPossession = new Selector<CommandPlayer>("GOAL KEEPER BALL POSSESSION");
		// check if the goal keeper has the ball possession
		Sequence<CommandPlayer> seqGKBallPossession = new Sequence<CommandPlayer>("GOAL KEEPER BALL POSSESSION");
		seqGKBallPossession.add(new BallPossession());
		// goal keeper behavior with the ball
		Selector<CommandPlayer> selGKKickOrPass = new Selector<CommandPlayer>("GOAL KEEPER KICK OR PASS");
		Sequence<CommandPlayer> seqGKKickOrPass = new Sequence<CommandPlayer>("GOAL KEEPER KICK OR PASS");
		seqGKKickOrPass.add(new GetClosestFreePlayer());
		seqGKKickOrPass.add(new PassBall());
		selGKKickOrPass.add(seqGKKickOrPass);
		selGKKickOrPass.add(new DoFreeKick());
		seqGKBallPossession.add(selGKKickOrPass);
		selGKBallPossession.add(seqGKBallPossession);

		// goal keeper behavior without the ball
		Sequence<CommandPlayer> seqGKBallArea = new Sequence<CommandPlayer>("GOAL KEEPER BALL AREA");
		seqGKBallArea.add(new BallArea());
		Selector<CommandPlayer> selGKBallClose = new Selector<CommandPlayer>("GOAL KEEPER BALL CLOSE");
		selGKBallClose.add(new DefendBall());
		selGKBallClose.add(new PrepareToDefend());
		seqGKBallArea.add(selGKBallClose);
		selGKBallPossession.add(seqGKBallArea);
		selGKBallPossession.add(new GoToHome());

		seqGoalKeeper.add(selGKBallPossession);

		// player behavior depends on ball possession
		Selector<CommandPlayer> selBallPossession = new Selector<CommandPlayer>("BALL POSSESSION");
		// check if the player has the ball possession
		Sequence<CommandPlayer> seqBallPossession = new Sequence<CommandPlayer>("BALL POSSESSION");
		seqBallPossession.add(new BallPossession());
		// player behavior with the ball
		Selector<CommandPlayer> selBallBehavior = new Selector<CommandPlayer>("BALL BEHAVIOR");
		selBallBehavior.add(new KickToGoal());
		Sequence<CommandPlayer> seqPassBall = new Sequence<CommandPlayer>("PASS BALL");
		seqPassBall.add(new IsEnemyClose());
		seqPassBall.add(new GetClosestFreePlayer());
		seqPassBall.add(new PassBall());
		selBallBehavior.add(seqPassBall);
		selBallBehavior.add(new AdvanceWithBall());
		seqBallPossession.add(selBallBehavior);
		selBallPossession.add(seqBallPossession);

		// player behavior without the ball depends on team ball possession
		Selector<CommandPlayer> selTeamBallPossession = new Selector<CommandPlayer>("TEAM BALL POSSESSION");
		// check if the team has the ball
		Sequence<CommandPlayer> seqTeamBallPossession = new Sequence<CommandPlayer>("TEAM BALL POSSESSION");
		seqTeamBallPossession.add(new TeamBallPossession());
		// player behavior when its team has the ball
		Selector<CommandPlayer> selAttackBehavior = new Selector<CommandPlayer>("ATTACK BEHAVIOR");
		Sequence<CommandPlayer> seqFollowBallPlayer = new Sequence<CommandPlayer>("FOLLOW BALL PLAYER");
		seqFollowBallPlayer.add(new IsCloseToBall());
		seqFollowBallPlayer.add(new FollowBallPlayer());
		selAttackBehavior.add(seqFollowBallPlayer);
		selAttackBehavior.add(new FollowBehind());
		selAttackBehavior.add(new FollowInFrontOf());
		seqTeamBallPossession.add(selAttackBehavior);
		selTeamBallPossession.add(seqTeamBallPossession);

		// player behavior when its team doesn't have the ball
		Sequence<CommandPlayer> seqGetBall = new Sequence<CommandPlayer>("GET BALL");
		seqGetBall.add(new IsCloseToBall());
		seqGetBall.add(new ChaseBall());
		selTeamBallPossession.add(seqGetBall);
		Sequence<CommandPlayer> seqMarkEnemy = new Sequence<CommandPlayer>("MARK ENEMY");
		seqMarkEnemy.add(new IsEnemyClose());
		seqMarkEnemy.add(new MarkEnemy());
		selTeamBallPossession.add(seqMarkEnemy);
		selTeamBallPossession.add(new GoToHome());
		selBallPossession.add(selTeamBallPossession);

		raiz.add(seqBeforeKickOff);
		raiz.add(seqKickOff);
		raiz.add(seqGoalKick);
		raiz.add(seqFreeKick);
		raiz.add(seqCornerKick);
		raiz.add(seqOtherState);
		raiz.add(seqGoalKeeper);
		raiz.add(selBallPossession);

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
			homePosition.setX(getHomePosition().getX() * selfPerc.getSide().value());
			homePosition.setY(getHomePosition().getY() * selfPerc.getSide().value());
		}
		updatePerceptions();

		while (commander.isActive()) {

			myTeam = fieldPerc.getTeamPlayers(getSide());
			enemyTeam = fieldPerc.getTeamPlayers(getSide().invert(getSide()));
			
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

	public Vector2D getGoalPos() {
		return goalPosition;
	}

	public Vector2D getPosition() {
		return selfPerc.getPosition();
	}

	public PlayerPerception getPlayerToPass() {
		return playerToPass;
	}

	public void setPlayerToPass(PlayerPerception playerToPass) {
		this.playerToPass = playerToPass;
	}

	public PlayerPerception getClosestEnPlayer() {
		return closestEnPlayer;
	}

	public void setClosestEnPlayer(PlayerPerception closestEnPlayer) {
		this.closestEnPlayer = closestEnPlayer;
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
		if (!isAlignToPoint(point, 15))
			turnToPoint(point);
		// commander.doMove(x, y)
		commander.doDash(speed);
	}

	public boolean isPointsAreClose(Vector2D reference, Vector2D point, double margin) {
		return reference.distanceTo(point) <= margin;
	}
}
