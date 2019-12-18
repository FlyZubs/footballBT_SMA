package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;

public class GoToHome extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		
		if (agent.isPointsAreClose(agent.selfPerc.getPosition(), agent.getHomePosition(), 1)) {
			return BTStatus.SUCCESS;
		} else {
			agent.dash(agent.getHomePosition(), 100);
			return BTStatus.RUNNING;
		}
	}

}
