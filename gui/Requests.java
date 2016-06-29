package com.dyn.student.gui;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;
import com.rabbit.gui.background.DefaultBackground;
import com.rabbit.gui.component.control.Button;
import com.rabbit.gui.component.control.MultiTextbox;
import com.rabbit.gui.component.control.TextBox;
import com.rabbit.gui.component.display.TextLabel;
import com.rabbit.gui.render.TextAlignment;
import com.rabbit.gui.show.Show;

import net.minecraft.client.Minecraft;

public class Requests extends Show {

	private static File getTimestampedFileForDirectory() {
		File file = new File(Minecraft.getMinecraft().mcDataDir, "/logs/");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
		String s = "request-" + dateFormat.format(new Date()).toString();
		int i = 1;

		while (true) {
			File file1 = new File(file, s + (i == 1 ? "" : "_" + i) + ".json");

			if (!file1.exists()) {
				return file1;
			}

			++i;
		}
	}

	TextBox requestTitle;

	MultiTextbox requestExplain;

	public Requests() {
		setBackground(new DefaultBackground());
		title = "Student Gui";
	}

	@Override
	public void setup() {

		registerComponent(new TextLabel(width / 3, (int) (height * .1), width / 3, 20, "Student UI Requests",
				TextAlignment.CENTER));
		registerComponent(new TextLabel(width / 3, (int) (height * .15), width / 3, 20, "What tools do you want?",
				TextAlignment.CENTER));

		registerComponent(
				requestTitle = new TextBox(width / 6, (int) (height * .25), width / 3, 20, "Name of Request"));

		registerComponent(requestExplain = new MultiTextbox(width / 6, (int) (height * .35), (int) (width * .66), 100,
				"Explain your Request"));

		// GUI main section
		registerComponent(
				new Button((int) (width * .6), (int) (height * .8), 150, 20, "Send Request").setClickListener(but -> {
					if (!requestTitle.getText().isEmpty() && !requestTitle.getText().equals("Name of Request")
							&& !requestExplain.getText().isEmpty()
							&& !requestExplain.getText().equals("Explain your Request")) {
						JsonObject request = new JsonObject();
						request.addProperty("title", requestTitle.getText());
						request.addProperty("request", requestExplain.getText());
						try {
							FileWriter fileWrite = new FileWriter(getTimestampedFileForDirectory());

							fileWrite.write(request.toString());
							System.out.println("Successfully Copied JSON Object to File...");
							System.out.println("\nJSON Object: " + request);
							fileWrite.close();
							stage.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}));
	}
}
