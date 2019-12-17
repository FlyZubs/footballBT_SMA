package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;

public class FollowInFrontOf extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (agent.isPointsAreClose(agent.getPosition(), agent.getGoalPos(), 15.0d)) {
			return BTStatus.SUCCESS;
		} else {
			Vector2D goTo = new Vector2D(agent.getGoalPos().getX(), agent.getPosition().getY());
			agent.dash(goTo, 60);
			return BTStatus.RUNNING;
		}
	}

}
