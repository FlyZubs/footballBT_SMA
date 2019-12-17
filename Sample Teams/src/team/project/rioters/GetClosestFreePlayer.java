package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.PlayerPerception;

public class GetClosestFreePlayer extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		PlayerPerception closestFreePlayer = agent.selfPerc;
		PlayerPerception closestPlayer = agent.selfPerc;
		
		
		double closestFreeDistance = Double.MAX_VALUE;
		double closestDistance = Double.MAX_VALUE;
		boolean canPass = true;
		for (PlayerPerception player : agent.myTeam) {
			double playerDistance = player.getPosition().distanceTo(agent.getBallPos());
			if (playerDistance > 1 && player.getUniformNumber() != 1) {
				if (playerDistance < closestFreeDistance) {
					if(playerDistance < closestDistance) {
						closestDistance = playerDistance;
						closestPlayer = player;
					}
					for (PlayerPerception enPlayer : agent.enemyTeam) {
						if (player.getPosition().distanceTo(enPlayer.getPosition()) < 5) {
							canPass = false;
						} else {
							canPass = true;
						}
					}
					if (canPass) {
						closestFreeDistance = playerDistance;
						closestFreePlayer = player;
					}
				}
			}
		}

		agent.setPlayerToPass(closestFreePlayer);

		if (agent.playerToPass == agent.selfPerc) {
			agent.setPlayerToPass(closestPlayer);
			return BTStatus.FAILURE;
		} else {
			return BTStatus.SUCCESS;
		}
	}
}
