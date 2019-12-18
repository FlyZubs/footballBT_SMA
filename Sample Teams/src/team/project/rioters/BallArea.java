package team.project.rioters;

import java.awt.Rectangle;

import behavior_tree.BTNode;
import behavior_tree.BTStatus;
import easy_soccer_lib.utils.EFieldSide;

public class BallArea extends BTNode<CommandPlayer> {

	@Override
	public BTStatus tick(CommandPlayer agent) {
		double ballX = agent.getBallPos().getX();
		double ballY = agent.getBallPos().getY();
		Rectangle area = agent.getSide() == EFieldSide.LEFT ? new Rectangle(-52, -20, 40, 40)
				: new Rectangle(12, -20, 40, 40);

		if (area.contains(ballX, ballY)) {
			return BTStatus.SUCCESS;
		} else {
			return BTStatus.FAILURE;
		}
	}

}