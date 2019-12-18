package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;

public class MoveToHome extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		
		if (agent.isPointsAreClose(agent.selfPerc.getPosition(), agent.getHomePosition(), 1)) {
			return BTStatus.SUCCESS;
		} else {
			agent.getCommander().doMoveBlocking(agent.getHomePosition());
			return BTStatus.RUNNING;
		}
	}

}
