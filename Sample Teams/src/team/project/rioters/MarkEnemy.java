package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;

public class MarkEnemy extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		if(agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 15)) {
			if(agent.isPointsAreClose(agent.getPosition(), agent.closestEnPlayer.getPosition(), 2)) {
				return BTStatus.SUCCESS;
			}else {
				agent.dash(agent.closestEnPlayer.getPosition(), 100);
				return BTStatus.RUNNING;
			}
		}else {
			return BTStatus.FAILURE;
		}
	}

}
