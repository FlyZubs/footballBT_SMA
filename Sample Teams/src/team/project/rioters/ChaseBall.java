package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;

public class ChaseBall extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if (agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 1.0d)) {
			return BTStatus.SUCCESS;
		} else {
			agent.dash(agent.getBallPos(), 90);
			return BTStatus.RUNNING;
		}
	}

}
