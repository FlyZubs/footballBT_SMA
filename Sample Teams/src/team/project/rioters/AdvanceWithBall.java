package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;

public class AdvanceWithBall extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (!agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 10.0d))
			return BTStatus.FAILURE;
		agent.commander.doKickToPoint(40.0d, agent.getGoalPos());
		return BTStatus.SUCCESS;
	}

}
