package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.PlayerPerception;

public class IsSecondClosest extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		double closestDistance = Double.MAX_VALUE;
		PlayerPerception closestPlayer = null, secondClosestPlayer = null;
		
		for (PlayerPerception player: agent.myTeam) {
			double playerDistance = player.getPosition().distanceTo(agent.getBallPos());
			if (agent.isPointsAreClose(player.getPosition(), agent.getBallPos(), 3.0d)) {
				if (playerDistance < closestDistance) {
					closestDistance = playerDistance;
					secondClosestPlayer = closestPlayer;
					closestPlayer = player;
				}
			}
		}
		
		if (secondClosestPlayer == agent.selfPerc) {
			return BTStatus.SUCCESS;
		}
		
		return BTStatus.FAILURE;
	}

}
