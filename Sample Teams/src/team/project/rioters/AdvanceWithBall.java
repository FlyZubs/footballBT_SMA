package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;

public class AdvanceWithBall extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 10.0d))
			return BTStatus.FAILURE;
		if (agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 3.0d)) {
			agent.dash(agent.getBallPos(), 60);
			return BTStatus.RUNNING;
		}
		agent.commander.doKickToPoint(40.0d, agent.getGoalPos());
		return BTStatus.SUCCESS;
	}

}
