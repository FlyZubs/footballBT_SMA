package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;

public class DoFreeKick extends BTNode<CommandPlayer> {

	public BTStatus tick(CommandPlayer agent) {
		if(agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 1)) {
			agent.commander.doKickToPoint(100, agent.getGoalPos());
			return BTStatus.SUCCESS;
		}else {
			agent.dash(agent.getBallPos(), 70);
			return BTStatus.RUNNING;
		}
	}
}
