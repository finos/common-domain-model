namespace Rosetta.Lib.Attributes
{
    using System;

    /// <summary>
    /// Specifies the original name for this object used by ISDA CDM.
    /// </summary>
    [AttributeUsage(AttributeTargets.Enum, Inherited = false)]
    public class CdmName : Attribute
    {
        public CdmName(string value)
        {
            Value = value;
        }

        public string Value { get; }
    }
}
