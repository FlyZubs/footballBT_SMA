package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.perception.PlayerPerception;

public class IsCloseToBall extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		double closestDistance = Double.MAX_VALUE;
		PlayerPerception closestPlayer = agent.selfPerc, secondClosestPlayer = agent.selfPerc;

		for (PlayerPerception player : agent.myTeam) {
			double playerDistance = player.getPosition().distanceTo(agent.getBallPos());
			if (playerDistance < closestDistance) {
				closestDistance = playerDistance;
				secondClosestPlayer = closestPlayer;
				closestPlayer = player;
			}
		}

		if (closestPlayer.getUniformNumber() == agent.selfPerc.getUniformNumber()
				|| secondClosestPlayer.getUniformNumber() == agent.selfPerc.getUniformNumber()) {
			return BTStatus.SUCCESS;
		}
		return BTStatus.FAILURE;
	}

}
