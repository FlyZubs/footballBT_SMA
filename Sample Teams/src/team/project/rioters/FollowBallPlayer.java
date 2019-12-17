package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;

public class FollowBallPlayer extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 7.0d)) {
			return BTStatus.SUCCESS;
		} else {
			if (agent.getPosition().getY() < agent.getBallPos().getY()) {
				Vector2D goTo = new Vector2D(agent.getBallPos().getX(), agent.getBallPos().getY() - 7.0d);
				agent.dash(goTo, 60);
			} else {
				Vector2D goTo = new Vector2D(agent.getBallPos().getX(), agent.getBallPos().getY() + 7.0d);
				agent.dash(goTo, 60);
			}
			return BTStatus.RUNNING;
		}
	}

}
