/* ===========================================================================
 * Copyright 2020 Trikzon
 *
 * Transparent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * File: ISetTransparent.java
 * Date: 2020-04-20 "1.15.2-1.0.0"
 * Revision:
 * Author: Trikzon
 * =========================================================================== */
package io.github.trikzon.transparent;

public interface ISetTransparent
{
    void setTransparent(boolean transparent, boolean isGlass);

    void reset();
}
