package team.project.rioters;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;

public class PassBall extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {

		if (!agent.isPointsAreClose(agent.getPosition(), agent.getBallPos(), 3)) {
			return BTStatus.FAILURE;
		}

		Vector2D targetPosition = agent.playerToPass.getPosition();
		Vector2D distance = targetPosition.sub(agent.getPosition());
		double intensity = ((distance.magnitude() * 90) / 40);
		agent.commander.doKickToPoint(intensity, targetPosition);
		
		if(agent.isPointsAreClose(targetPosition, agent.getBallPos(), 3)) {
			return BTStatus.SUCCESS;
		}
		
		return BTStatus.RUNNING;
		
	}
}
