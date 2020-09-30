namespace Rosetta.Lib.Attributes
{
    using System;

    /// <summary>
    /// Specifies that only one of the properties of the class should ever be set.
    /// </summary>
    [AttributeUsage(AttributeTargets.Class, Inherited = false)]
    public class OneOfAttribute : Attribute
    {

    }
}
