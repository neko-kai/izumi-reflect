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

package izreflect.fundamentals.reflection

import izreflect.fundamentals.platform.console.TrivialLogger
import izreflect.fundamentals.platform.console.TrivialLogger.Config

import scala.reflect.ClassTag
import scala.reflect.macros.blackbox

/**
  * To see macro debug output during compilation, set `-Dizreflect.debug.macro.rtti=true` java property! e.g.
  *
  * {{{
  * sbt -Dizreflect.debug.macro.rtti=true compile
  * }}}
  *
  * @see [[DebugProperties]]
  */
object TrivialMacroLogger {
  def make[T: ClassTag](c: blackbox.Context, sysProperty: String): TrivialLogger = {
    TrivialLogger.make[T](sysProperty, config = Config(sink = new ScalacSink(c)))
  }
}
