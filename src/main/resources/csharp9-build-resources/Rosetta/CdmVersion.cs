namespace Rosetta.Lib.Attributes
{
    using System;

    /// <summary>
    /// Specifies the version of ISDA CDM used.
    /// </summary>
    [AttributeUsage(AttributeTargets.Assembly, Inherited = false)]
    public class CdmVersion : Attribute
    {
        public CdmVersion(string value)
        {
            Value = value;
        }

        public string Value { get; }
    }
}
