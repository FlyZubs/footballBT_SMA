package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;

public class DefendBall extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 3.0d)) {
			if (agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 1.0d)) {
				agent.commander.doKickToPoint(100, agent.goalPosition);
				return BTStatus.SUCCESS;
			} else {
				agent.dash(agent.getBallPos(), 100);
				return BTStatus.RUNNING;
			}
		}
		return BTStatus.FAILURE;
	}

}
