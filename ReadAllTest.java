/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package org.app;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.Thread;
import java.lang.System;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.server.datanode.SimulatedFSDataset;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class ReadAllTest extends Configured {
  private static int readAt(FSDataInputStream fis, long at) throws IOException {
    fis.seek(at);
    return fis.read();
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.out.println("ReadAllTest: must supply the HDFS uri and file to read");
      System.exit(1);
    }
    String hdfsUri = args[0];
    String fileToRead = args[1];
    final Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(new URI(hdfsUri), conf);

    byte ORIGINAL[] = new byte[10];
    for (int i = 0; i < ORIGINAL.length; i++) {
      ORIGINAL[i] = (byte)i;
    }
    FSDataOutputStream out = fs.create(new Path("/b"), (short)1);
    try {
      out.write(ORIGINAL);
    } finally {
      out.close();
    }
    byte input[] = new byte[ORIGINAL.length];
    FSDataInputStream in = fs.open(new Path("/b"));
    try {
      in.readFully(input);
    } finally {
      in.close();
    }
    in = fs.open(new Path("/b"));
    try {
      in.readFully(0, input);
    } finally {
      in.close();
    }
  }
}
