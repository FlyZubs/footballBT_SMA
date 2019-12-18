package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;

public class DoCornerKick extends BTNode<CommandPlayer> {

	public BTStatus tick(CommandPlayer agent) {
		if (agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 1)) {
			agent.commander.doKickToPoint(100, agent.getGoalPos().sub(new Vector2D(5*agent.getSide().value(),0)));
			return BTStatus.SUCCESS;
		} else {
			agent.dash(agent.getBallPos(), 70);
			return BTStatus.RUNNING;
		}
	}
}
