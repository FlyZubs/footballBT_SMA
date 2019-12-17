package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;

public class KickToGoal extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (agent.isPointsAreClose(agent.getPosition(), agent.getGoalPos(), 25.0d)) {
			agent.commander.doKickToPoint(100.0d, agent.getGoalPos());
			return BTStatus.SUCCESS;
		}
		return BTStatus.FAILURE;
	}

}
