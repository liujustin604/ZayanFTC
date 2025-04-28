package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;

@SuppressWarnings("unused")
@TeleOp(name = "TeleOp")
public class ZayanTeleOp extends LinearOpMode {
	private FtcDashboard dash = FtcDashboard.getInstance();
	private ArrayList<Action> runningActions = new ArrayList<>();
	private MecanumDrive drive;
	private boolean toggleSlow = true;
	@Override
	public void runOpMode() {
		drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
		waitForStart();

		while (opModeIsActive()) {
			runActions();
			drive.updatePoseEstimate();
			double multiplier = toggleSlow ? 0.4 : 1;

			Vector2d linear = new Vector2d(gamepad1.left_stick_x, gamepad1.left_stick_y);
			drive.setDrivePowers(new PoseVelocity2d(
					linear.times(multiplier),
					gamepad1.right_stick_x * multiplier
			));

			if (gamepad1.y) {
				toggleSlow = !toggleSlow;
			}
		}

	}

	private void runActions() {
		TelemetryPacket packet = new TelemetryPacket();

		ArrayList<Action> newActions = new ArrayList<>();
		for (Action action : runningActions) {
			action.preview(packet.fieldOverlay());
			if (action.run(packet)) {
				newActions.add(action);
			}
		}
		runningActions = newActions;

		dash.sendTelemetryPacket(packet);
	}
}
