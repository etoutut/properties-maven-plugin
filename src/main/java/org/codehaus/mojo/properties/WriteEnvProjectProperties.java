package org.codehaus.mojo.properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import java.util.Properties;

/**
 * Writes project properties to a file.
 * Retrieves only properties with env prefix.
 * Used in association with keyPrefix attribute on ReadPropertiesMojo,
 * to isolate properties for specific environment.
 * @version $Id$
 */
@Mojo( name = "write-env-project-properties", defaultPhase = LifecyclePhase.NONE, threadSafe = true )
public class WriteEnvProjectProperties
    extends AbstractWritePropertiesMojo
{

    /** {@inheritDoc} */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        validateOutputFile();
        Properties projProperties = new Properties();

        // Suppress prefix from key
        if (null != keyPrefix && keyPrefix.length() > 0) {
            for (Object k : getProject().getProperties().keySet()) {
                String key = (String) k;
                String value = getProject().getProperties().getProperty(key);
                if (key.startsWith(keyPrefix)) {
                    projProperties.put(key.substring(keyPrefix.length()), value);
                }
            }
        }

        // stores K/V in outputfile
        writeProperties( projProperties, getOutputFile() );
    }
}
