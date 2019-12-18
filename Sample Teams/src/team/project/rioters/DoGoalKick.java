package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;

public class DoGoalKick extends BTNode<CommandPlayer> {

	public BTStatus tick(CommandPlayer agent) {
		if (agent.selfPerc.getUniformNumber() == 7) {
			agent.dash(agent.getBallPos(), 70);
			if (agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 1)) {
				agent.commander.doKickToPoint(60, agent.getGoalPos());
				return BTStatus.SUCCESS;
			}
			return BTStatus.RUNNING;
		} else {
			return BTStatus.FAILURE;
		}
	}
}