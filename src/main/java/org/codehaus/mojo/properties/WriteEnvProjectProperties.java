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
                    // specific environment property always retrieved
                    projProperties.put(key.substring(keyPrefix.length()), value);
                } else {
                    // property with common prefix retrieved only if doesnt exit with
                    // prefix specific to environment
                    if (key.startsWith(commonKeyPrefix)) {
                        String keyWithoutCommonPrefix = key.substring(commonKeyPrefix.length());
                        String keyWithSpecificPrefix = keyPrefix + keyWithoutCommonPrefix;
                        if (!getProject().getProperties().containsKey(keyWithSpecificPrefix)) {
                            projProperties.put(keyWithoutCommonPrefix, value);
                        }
                    }
                }
            }
        }

        // stores K/V in outputfile
        writeProperties( projProperties, getOutputFile() );
    }
}
