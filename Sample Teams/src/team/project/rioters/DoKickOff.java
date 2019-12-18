package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;

public class DoKickOff extends BTNode<CommandPlayer> {

	public BTStatus tick(CommandPlayer agent) {
		Vector2D centerPos = new Vector2D(0, 0);
		if (agent.selfPerc.getUniformNumber() == 1) {
			agent.dash(centerPos, 70);
			if (agent.isPointsAreClose(agent.getPosition(), centerPos, 1)) {
				Vector2D mygoal = agent.getGoalPos().multiply(-1);
				agent.commander.doKickToPoint(60, mygoal);
				return BTStatus.SUCCESS;
			}
			return BTStatus.RUNNING;
		} else {
			return BTStatus.FAILURE;
		}
	}
}
