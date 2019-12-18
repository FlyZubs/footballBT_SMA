package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.PlayerPerception;

public class TeamBallPossession extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		PlayerPerception closestPlayer = agent.selfPerc;

		double closestDistance = Double.MAX_VALUE;
		double closestEnDistance = Double.MAX_VALUE;

		for (PlayerPerception player : agent.myTeam) {
			double playerDistance = player.getPosition().distanceTo(agent.getBallPos());
			if (agent.isPointsAreClose(player.getPosition(), agent.getBallPos(), 5.0d)) {
				if (playerDistance < closestDistance) {
					closestPlayer = player;
					closestDistance = playerDistance;
				}
			}
		}
		for (PlayerPerception enPlayer : agent.enemyTeam) {
			double enPlayerDistance = enPlayer.getPosition().distanceTo(agent.getBallPos());
			if (enPlayerDistance < closestEnDistance) {
				closestEnDistance = enPlayerDistance;
			}
		}

		if (closestDistance < closestEnDistance
				&& closestPlayer.getUniformNumber() != agent.selfPerc.getUniformNumber()) {
			return BTStatus.SUCCESS;
		}
		return BTStatus.FAILURE;
	}

}
