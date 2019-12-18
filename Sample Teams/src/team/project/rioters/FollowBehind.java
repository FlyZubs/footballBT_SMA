package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;

public class FollowBehind extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if ((agent.getPosition().getX() * agent.getSide().value()) <= agent.getBallPos().getX() * agent.getSide().value()) {
			if (agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 30.0d)) {
				return BTStatus.SUCCESS;
			} else {
				Vector2D goTo = new Vector2D(agent.getBallPos().getX(), agent.getPosition().getY());
				agent.dash(goTo, 60);
				return BTStatus.RUNNING;
			}
		}
		return BTStatus.FAILURE;
	}

}
