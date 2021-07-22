package org.codehaus.mojo.properties;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file 
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY 
 * KIND, either express or implied.  See the License for the 
 * specific language governing permissions and limitations 
 * under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * @author <a href="mailto:zarars@gmail.com">Zarar Siddiqi</a>
 * @version $Id$
 */
public abstract class AbstractWritePropertiesMojo
    extends AbstractMojo
{

    @Parameter( defaultValue = "${project}", required = true, readonly = true )
    private MavenProject project;

    @Parameter( required = true )
    private File outputFile;

    /**
     * Prefix to be used before name of each property.
     * Can be useful to extract properties by environment.
     */
    @Parameter
    protected String keyPrefix = null;

    public void setKeyPrefix( String keyPrefix )
    {
        this.keyPrefix = keyPrefix;
    }

    /**
     * Prefix to be used before name of each property common to many environments.
     * Can be useful to extract properties by environment.
     * Vaue overriden by same Key for specific environment.
     */
    @Parameter( defaultValue = "*.")
    protected String commonKeyPrefix = null;

    public void setCommonKeyPrefix( String commonKeyPrefix )
    {
        this.commonKeyPrefix = commonKeyPrefix;
    }

    /**
     * @param properties {@link Properties}
     * @param file {@link File}
     * @throws MojoExecutionException {@link MojoExecutionException}
     */
    protected void writeProperties( Properties properties, File file )
        throws MojoExecutionException
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream( file );
            properties.store( fos, "Properties" );
        }
        catch ( FileNotFoundException e )
        {
            getLog().error( "Could not create FileOutputStream: " + fos );
            throw new MojoExecutionException( e.getMessage(), e );
        }
        catch ( IOException e )
        {
            getLog().error( "Error writing properties: " + fos );
            throw new MojoExecutionException( e.getMessage(), e );
        }

        try
        {
            fos.close();
        }
        catch ( IOException e )
        {
            getLog().error( "Error closing FileOutputStream: " + fos );
            throw new MojoExecutionException( e.getMessage(), e );
        }
    }

    /**
     * @throws MojoExecutionException {@link MojoExecutionException}
     */
    protected void validateOutputFile()
        throws MojoExecutionException
    {
        if ( outputFile.isDirectory() )
        {
            throw new MojoExecutionException( "outputFile must be a file and not a directory" );
        }
        // ensure path exists
        if ( outputFile.getParentFile() != null )
        {
            outputFile.getParentFile().mkdirs();
        }
    }

    /**
     * @return {@link MavenProject}
     */
    public MavenProject getProject()
    {
        return project;
    }

    /**
     * @return {@link #outputFile}
     */
    public File getOutputFile()
    {
        return outputFile;
    }

}
