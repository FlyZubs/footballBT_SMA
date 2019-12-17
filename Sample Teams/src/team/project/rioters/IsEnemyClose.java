package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.PlayerPerception;

public class IsEnemyClose extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		for (PlayerPerception enPlayer : agent.enemyTeam) {
			if (agent.isPointsAreClose(agent.getPosition(), enPlayer.getPosition(), 5.0d)) {
				return BTStatus.SUCCESS;
			}
		}
		return BTStatus.FAILURE;
	}

}
