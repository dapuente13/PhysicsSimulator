package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/*
 * Examples of command-line parameters:
 * 
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.5body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.FallingToCenterGravityBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoGravityBuilder;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static int _numstepsDefaultValue = 150;
	private final static String _modeDefaultValue = "batch";

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;
	private static JSONObject _gravityLawsInfo = null;
	private static int _numsteps = 0;
	
	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<GravityLaws> _gravityLawsFactory;

	private static void init() {
		// initialize the bodies factory
		// ...
		
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder<Body>());
		bodyBuilders.add(new MassLosingBodyBuilder<Body>());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);

		// initialize the gravity laws factory
		// ...
		
		ArrayList<Builder<GravityLaws>> gravityBuilders = new ArrayList<>();
		gravityBuilders.add(new FallingToCenterGravityBuilder<GravityLaws>());
		gravityBuilders.add(new NewtonUniversalGravitationBuilder<GravityLaws>());
		gravityBuilders.add(new NoGravityBuilder<GravityLaws>());
		_gravityLawsFactory = new BuilderBasedFactory<GravityLaws>(gravityBuilders);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options 
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseDeltaTimeOption(line);
			parseGravityLawsOption(line);
			parseNumStepsOption(line);
			parseMode(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();
		
		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());
		
		// output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is writen. Default value: the standard output").build());
		
		// steps number
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("An integer representing the number of simulation steps. Default value: 150.").build());
		
		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());
		
		// steps number
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: 'batch' (Batch mode), 'gui' (Graphical User Inteface mode). Default value: 'batch'.").build());
				

		// gravity laws -- there is a workaround to make it work even when
		// _gravityLawsFactory is null. 
		//
		String gravityLawsValues = "N/A";
		String defaultGravityLawsValue = "N/A";
		if (_gravityLawsFactory != null) {
			gravityLawsValues = "";
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gravityLawsValues.length() > 0) {
					gravityLawsValues = gravityLawsValues + ", ";
				}
				gravityLawsValues = gravityLawsValues + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
			}
			defaultGravityLawsValue = "nlug";
		}
		cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
				.desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
						+ ". Default value: '" + defaultGravityLawsValue + "'.")
				.build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line){
	
		_inFile = line.getOptionValue("i");
		
	}
	
	private static void parseOutFileOption(CommandLine line){
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseNumStepsOption(CommandLine line) throws ParseException {
		
		String st = line.getOptionValue("s", _numstepsDefaultValue+"");
		
		try {
			_numsteps = Integer.parseInt(st);
			assert (_numsteps > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid steps value: " + st);
		}
	
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
		
	}

	private static void parseGravityLawsOption(CommandLine line) throws ParseException {

		String gl = null;
		
		// this line is just a work around to make it work even when _gravityLawsFactory
		// is null, you can remove it when've defined _gravityLawsFactory
		
		//if (_gravityLawsFactory == null)
		//	return;

		gl = line.getOptionValue("gl");
		if (gl != null) {
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gl.equals(fe.getString("type"))) {
					_gravityLawsInfo = fe;
					break;
				}
			}
			if (_gravityLawsInfo == null) {

				throw new ParseException("Invalid gravity laws: " + gl);
			}
		} else {
			_gravityLawsInfo = _gravityLawsFactory.getInfo().get(1);
		}
	
	}
	
	private static void parseMode(CommandLine line) throws ParseException {
		String mode = line.getOptionValue("m", _modeDefaultValue);
				
		if (mode.equals("batch") || mode.equals("gui")){
			_mode = mode;
		}
		else {
			throw new ParseException("Invalid mode: " + mode);
		}
		
	}

	private static void startBatchMode() throws FileNotFoundException {
				
		PhysicsSimulator ps = new PhysicsSimulator(_gravityLawsFactory.createInstance(_gravityLawsInfo), _dtime);
		Controller control = new Controller(ps, _bodyFactory, _gravityLawsFactory);
				
		//Create the corresponding input and output streams from the values of options -i and -o
		
		InputStream in = new FileInputStream(_inFile);
		control.loadBodies(in);			//----Input Stream---- 
		
		OutputStream out = _outFile ==null ? System.out : new FileOutputStream(_outFile);
		control.run(_numsteps, out);			//----Output Stream----
	}
	
	private static void startGUIMode() throws FileNotFoundException {
		
		PhysicsSimulator ps = new PhysicsSimulator(_gravityLawsFactory.createInstance(_gravityLawsInfo), _dtime);
		Controller control = new Controller(ps, _bodyFactory, _gravityLawsFactory);
		
		if (_inFile != null) {

			InputStream in = new FileInputStream(_inFile);
			control.loadBodies(in);	
		}
		control.setSteps(_numsteps);
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new MainWindow(control);
				}
			});
		} catch (InvocationTargetException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}


	private static void start(String[] args) throws Exception {
		parseArgs(args);

		if (_mode.equals("batch")) {
			startBatchMode();
		}
		else {
			startGUIMode();
		}
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
