/*
 * Copyright 2019-2020 Septimal Mind Ltd
 * Copyright 2020 John A. De Goes and the ZIO Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package izumi.fundamentals.platform.cli

import izumi.fundamentals.platform.cli.CLIParser.ParserError
import izumi.fundamentals.platform.cli.CLIParser.ParserError.{DanglingArgument, DanglingSplitter, DuplicatedRoles}
import izumi.fundamentals.platform.language.Quirks

trait ParserFailureHandler {
  def onParserError(e: CLIParser.ParserError): Unit
}

object ParserFailureHandler {

  object TerminatingHandler extends ParserFailureHandler {
    override def onParserError(e: CLIParser.ParserError): Nothing = {
      System.err.println(makeMessage(e))
      System.exit(1)
      throw new IllegalStateException("System.exit() didn't work")
    }
  }

  object PrintingHandler extends ParserFailureHandler {
    override def onParserError(e: CLIParser.ParserError): Unit = {
      System.err.println(makeMessage(e))
    }
  }

  object NullHandler extends ParserFailureHandler {
    override def onParserError(e: CLIParser.ParserError): Unit = {
      Quirks.discard(e)
    }
  }

  private[this] def makeMessage(e: ParserError): String = {
    e match {
      case d: DanglingArgument =>
        s"""Improperly positioned argument '${d.arg}' after valid commandline '${d.processed.mkString(" ")}'
           |
           |$example
         """.stripMargin
      case d: DanglingSplitter =>
        s"""Improperly positioned splitter '--' after valid commandline '${d.processed.mkString(" ")}'
           |
           |$example
         """.stripMargin
      case d: DuplicatedRoles =>
        s"""Duplicated roles: ${d.bad.mkString(", ")}
           |
           |$example
         """.stripMargin
    }
  }

  final val example =
    s"""General commandline format:
       |
       |  [OPTION...] [ROLE...]
       |
       |    OPTION is one of the following:
       |
       |      --flag-name
       |      --value-name=value
       |      -flag
       |      -shortname value
       |
       |    ROLE is :role-name [OPTION...] [--] [<role-argument>...]
       |
       |Example:
       |
       |  -with-flag --option=value :my-role
       |  -with-flag --option=value :my-role --role-config=myconfig.json :other-role
       |  -with-flag --option=value :first-role --role-config=myconfig.json -- --role-option=value role args here \\
       |    :convert -- input.txt output.jpg
       |
       |Try ':help' to get application specific help
     """.stripMargin

}