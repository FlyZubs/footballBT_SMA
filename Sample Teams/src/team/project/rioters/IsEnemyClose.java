package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.PlayerPerception;

public class IsEnemyClose extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		PlayerPerception closestEnPlayer = null;
		double closestEnDistance = Double.MAX_VALUE;
		for (PlayerPerception enPlayer : agent.enemyTeam) {
			double enDistance = enPlayer.getPosition().distanceTo(agent.getBallPos());
			if (enDistance < closestEnDistance) {
				closestEnDistance = enDistance;
				closestEnPlayer = enPlayer;
			}
		}
		agent.setClosestEnPlayer(closestEnPlayer);
		if (agent.isPointsAreClose(agent.getPosition(), closestEnPlayer.getPosition(), 5.0d)) {
			return BTStatus.SUCCESS;
		}
		return BTStatus.FAILURE;
	}

}
